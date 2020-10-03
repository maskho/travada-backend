package com.travada.backend.module.user.service;

import com.cloudinary.utils.ObjectUtils;
import com.travada.backend.bank.service.BankService;
import com.travada.backend.config.CloudinaryConfig;
import com.travada.backend.config.security.JwtTokenProvider;
import com.travada.backend.config.security.UserPrincipal;
import com.travada.backend.exception.AppException;
import com.travada.backend.exception.ResourceNotFoundException;
import com.travada.backend.module.transaksi.model.Saldo;
import com.travada.backend.module.transaksi.repository.SaldoRepository;
import com.travada.backend.module.user.dto.*;
import com.travada.backend.module.user.model.Role;
import com.travada.backend.module.user.model.RoleName;
import com.travada.backend.module.user.model.User;
import com.travada.backend.module.user.repository.RoleRepository;
import com.travada.backend.module.user.repository.UserRepository;
import com.travada.backend.module.user.service.email.EmailService;
import com.travada.backend.utils.BaseResponse;
import com.travada.backend.utils.ModelMapperUtil;
import com.travada.backend.utils.crypto.EncoderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    RegistrationDto registrationDto = new RegistrationDto();

    private ModelMapperUtil modelMapperUtil = new ModelMapperUtil();

    @Autowired
    CloudinaryConfig cloudinary;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    BankService bankService;

    @Autowired
    EncoderHelper encoderHelper;

    @Autowired
    SaldoRepository saldoRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByConfirmationCode(String confirmationCode) {
        return userRepository.findByConfirmationCode(confirmationCode);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    //Registrasi User Baru
    @Override
    public ResponseEntity<?> createNewUser(CreateUserDto createUser,
                                           MultipartFile foto_ktp,
                                           MultipartFile selfie_ktp) {
        User user = modelMapperUtil
                .modelMapperInit()
                .map(createUser, User.class);

        try {

            //Lookup user in database by email and username
            User existEmail = findByEmail(createUser.getEmail());
            Optional<User> existUsername = findByUsername(createUser.getUsername());

            if (existEmail != null) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                null,
                                "Email sudah digunakan"),
                        HttpStatus.BAD_REQUEST);
            }
            if (existUsername.isPresent()) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                null,
                                "Username sudah digunakan"),
                        HttpStatus.BAD_REQUEST);
            }


            //Disable user until they confirm
            createUser.setActive(false);


            //Upload image to cloudinary
            try {
                Map uploadResult = cloudinary.upload(foto_ktp.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));

                user.setFotoKtp(uploadResult.get("url").toString());

                Map uploadResult2 = cloudinary.upload(selfie_ktp.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));

                user.setSelfieKtp(uploadResult2.get("url").toString());


            } catch (IOException e) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                null,
                                "Upload foto gagal"),
                        HttpStatus.BAD_REQUEST);
            }

            //Generate confirmation code
            Random random = new Random();
            int range = 9999 - 1000 + 1;
            int randomCode = random.nextInt(range) + 1000;
            String confirmationCode = Integer.toString(randomCode);
            createUser.setConfirmationCode(confirmationCode);

            //Set encode password

            String encodedPassword = passwordEncoder.encode(createUser.getPassword());
            user.setPassword(encodedPassword);

            user.setNamaLengkap(createUser.getNama_lengkap());
            user.setUsername(createUser.getUsername());
            user.setEmail(createUser.getEmail());
            user.setNoHp(createUser.getNo_hp());
            user.setNoRekening(createUser.getNo_rekening());
            user.setNoKtp(createUser.getNo_ktp());
            user.setTglLahir(createUser.getTgl_lahir());
            user.setJenisKelamin(createUser.getJenis_kelamin());
            user.setPin(createUser.getPin());
            user.setActive(createUser.isActive());
            user.setStatus("Pending");
            user.setAccepted(false);
            user.setConfirmationCode(createUser.getConfirmationCode());

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));

            user.setRoles(Collections.singleton(userRole));

            System.out.println(createUser.getEmail());
            System.out.println(user);

            //Save user
            saveUser(user);

            //Send confirmation email

            emailService.sendMail(createUser.getEmail(),
                    "Konfirmasi Pendaftaran Anda",
                    "Kode anda : " + user.getConfirmationCode());

            registrationDto.setUsername(createUser.getUsername());
            registrationDto.setEmail(createUser.getEmail());

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            registrationDto,
                            "Registrasi Berhasil, Kode konfirmasi berhasil dikirim ke " + createUser.getEmail()),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);
        }

    }

    @Override
    public ResponseEntity<?> confirmNewUser(ConfirmationDto confirmUser) {

        try {

            //Find the user associated with associated code
            User existUser = userRepository.findByUsername(confirmUser.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            ;

            String confirmCode = confirmUser.getConfirmationCode();
            String sentCode = existUser.getConfirmationCode();
            String rawPassword = confirmUser.getPassword();
            String encodedPassword = existUser.getPassword();
            boolean isPasswordMatch = passwordEncoder.matches(rawPassword, encodedPassword);

            System.out.println(confirmCode == sentCode);

            try {
                if (confirmCode.equals(sentCode) || existUser.isActive()) {

                    existUser.setActive(true);
                    existUser.setConfirmationCode("null");

                    userRepository.save(existUser);

                    if (!isPasswordMatch) {
                        return new ResponseEntity(new BaseResponse
                                (HttpStatus.BAD_REQUEST,
                                        null,
                                        "Username atau password salah"),
                                HttpStatus.BAD_REQUEST);
                    }

                    //Check if user active

                    boolean isActive = existUser.isActive();

                    if (!isActive) {
                        return new ResponseEntity(new BaseResponse
                                (HttpStatus.BAD_REQUEST,
                                        null,
                                        "Akun anda belum aktif"),
                                HttpStatus.BAD_REQUEST);
                    }

                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    confirmUser.getUsername(),
                                    confirmUser.getPassword()
                            )
                    );

                    String pin = existUser.getPin();

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    String jwt = tokenProvider.generateToken(authentication);
                    return new ResponseEntity(new BaseResponse
                            (HttpStatus.OK,
                                    new JwtAuthenticationResponse(jwt, encoderHelper.encryptBase64(pin)),
                                    "Login berhasil"),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity(new BaseResponse
                            (HttpStatus.BAD_REQUEST,
                                    null,
                                    "Kode yang anda masukkan tidak valid"),
                            HttpStatus.BAD_REQUEST);
                }


            } catch (Exception e) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_GATEWAY,
                                null,
                                "Username tidak ditemukan"),
                        HttpStatus.BAD_GATEWAY);
            }

        } catch (Exception e) {
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);

        }
    }

    @Override
    public ResponseEntity<?> resendCode(ResendCodeDto resendCodeDto) {
        try {

            //Find the user associated with email
            User existUser = userRepository.findByEmail(resendCodeDto.getEmail());

            //If user not found
            if (existUser == null) {

                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                null,
                                "Akun tidak ditemukan, silahkan daftar"),
                        HttpStatus.BAD_REQUEST);
            }

            //If user already active
            if (existUser.isActive()) {

                return new ResponseEntity(new BaseResponse
                        (HttpStatus.OK,
                                null,
                                "Akun anda sudah aktif, silahkan login"),
                        HttpStatus.OK);
            }
            //Set new code to the user
            //Generate confirmation code
            Random random = new Random();
            int range = 9999 - 1000 + 1;
            int randomCode = random.nextInt(range) + 1000;
            String confirmationCode = Integer.toString(randomCode);

            existUser.setConfirmationCode(confirmationCode);

            //Save user
            saveUser(existUser);

            //Send confirmation email
            emailService.sendMail(resendCodeDto.getEmail(),
                    "Konfirmasi Pendaftaran Anda", "Kode Konfirmasi Anda : " + existUser.getConfirmationCode());

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            resendCodeDto,
                            "Resend Kode Berhasil, Kode konfirmasi berhasil dikirim ke " + resendCodeDto.getEmail()),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginDto loginDto) {

        try {

            //Validate password

            Optional<User> existUser = findByUsername(loginDto.getUsername());
            if (existUser.isEmpty()) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                null,
                                "Username atau password salah"),
                        HttpStatus.BAD_REQUEST);
            }

            String rawPassword = loginDto.getPassword();
            String encodedPassword = existUser.get().getPassword();
            boolean isPasswordMatch = passwordEncoder.matches(rawPassword, encodedPassword);

            if (!isPasswordMatch) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                null,
                                "Username atau password salah"),
                        HttpStatus.BAD_REQUEST);
            }

            //Check if user active

            boolean isActive = existUser.get().isActive();

            if (!isActive) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                null,
                                "Akun anda belum aktif"),
                        HttpStatus.BAD_REQUEST);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            String pin = existUser.get().getPin();

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            new JwtAuthenticationResponse(jwt, encoderHelper.encryptBase64(pin)),
                            "Login berhasil"),
                    HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);
        }

    }

    @Override
    public ResponseEntity<?> checkRegistration(CheckRegisDto checkRegisDto) {
        try {

            //Lookup user in database by email and username
            User existEmail = findByEmail(checkRegisDto.getEmail());
            Optional<User> existUsername = findByUsername(checkRegisDto.getUsername());
            User existNohp = userRepository.findBynoHp(checkRegisDto.getNo_hp());

            DataCheckRegisDto dataCheckRegisDto = new DataCheckRegisDto();
            dataCheckRegisDto.setMsg1(null);
            dataCheckRegisDto.setMsg2(null);
            dataCheckRegisDto.setMsg3(null);
            dataCheckRegisDto.setMsg4(null);

            if ((existEmail != null) || (existUsername.isPresent()) || (existNohp != null) ) {
                //Check email
                if (existEmail != null) {
                    dataCheckRegisDto.setMsg2("Alamat email telah terdaftar");
                }

                //Check username
                if (existUsername.isPresent()) {
                    dataCheckRegisDto.setMsg1("Username  telah terdaftar");
                }
                //Check handpone
                if (existNohp != null) {
                    dataCheckRegisDto.setMsg3("Nomor handphone telah tetdaftar");
                }

                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                dataCheckRegisDto,
                                "Error"),
                        HttpStatus.BAD_REQUEST);
            }

            //Check Rekening
            //Pending

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            null,
                            "Succes"),
                    HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ResponseEntity<?> getMyAccount(UserPrincipal userPrincipal) {
        try{
            String currentUsername = userPrincipal.getUsername();
            User currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(ResourceNotFoundException::new);

            GetMyAccountDto getMyAccountDto = new GetMyAccountDto();
            System.out.println(currentUser.getNoRekening());
            System.out.println(currentUser.getPin());

            Saldo saldo = saldoRepository.findByUserId(currentUser.getId());
            System.out.println(saldo);
            Long balance;
            if(saldo == null) {
                balance = 0L;
            } else {
                balance = saldo.getSaldo();
            }

//          String rawbalance = bankService.getBalance(currentUser.getNoRekening(), currentUser.getPin());
            getMyAccountDto.setBalance(balance);
            getMyAccountDto.setNamaLengkap(currentUser.getNamaLengkap());
            getMyAccountDto.setId(currentUser.getId());
            getMyAccountDto.setEmail(currentUser.getEmail());
            getMyAccountDto.setNoRekening(currentUser.getNoRekening());
            String encodedPin = encoderHelper.encryptBase64(currentUser.getPin());
            getMyAccountDto.setPin(encodedPin);
            getMyAccountDto.setActive(currentUser.isActive());
            getMyAccountDto.setAccepted(currentUser.isAccepted());

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            getMyAccountDto,
                            "Data user loaded"),
                    HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);
        }
    }
}
