package com.travada.backend.module.nasabah.service;

import com.travada.backend.module.nasabah.dto.ListUserDto;
import com.travada.backend.module.nasabah.dto.NotifNewUserDto;
import com.travada.backend.module.user.model.User;
import com.travada.backend.module.user.repository.UserRepository;
import com.travada.backend.utils.BaseResponse;
import com.travada.backend.utils.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NasabahServiceImpl implements NasabahService {

    @Autowired
    private UserRepository userRepository;

    private ModelMapperUtil modelMapperUtil = new ModelMapperUtil();

    @Override
    public ResponseEntity<?> getAllUser(String status) {
        try {

            List<User> users = new ArrayList<>();
            List<ListUserDto> userDtoList = new ArrayList<>();

            System.out.println(status == null);

            if (status == null) {
                users = userRepository.findAll();
            }
            else if (status.equals("reject")) {
                users = userRepository.findByStatus("Ditolak");
            }
            else if (status.equals("confirm")) {
                users = userRepository.findByStatus("Diterima");
            }
            else if (status.equals("pending")) {
               users = userRepository.findByStatus("Pending");
            } else {
                users = userRepository.findAll();
            }

            System.out.println(users);
            for (User user : users) {
                ListUserDto userDto = modelMapperUtil
                        .modelMapperInit()
                        .map(user, ListUserDto.class);

                userDto.setId(user.getId());
                userDto.setUsername(user.getUsername());
                userDto.setEmail(user.getEmail());
                userDto.setNama_lengkap(user.getNamaLengkap());
                userDto.setJenis_kelamin(user.getJenisKelamin());
                userDto.setTgl_lahir(user.getTglLahir());
                userDto.setActive(user.isActive());
                userDto.setNo_hp(user.getNoHp());
                userDto.setNo_ktp(user.getNoKtp());
                userDto.setNo_rekening(user.getNoRekening());
                userDto.setFotoKtp(user.getFotoKtp());
                userDto.setSelfieKtp(user.getSelfieKtp());

                List<String> tagList = new ArrayList<>();

                tagList.add(user.getStatus());

                userDto.setTags(tagList);

                userDtoList.add(userDto);

            }

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            userDtoList,
                            "Data nasabah berhasil dimuat"),
                    HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            e.getMessage()),
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ResponseEntity<?> getDetailUser(Long id) {
        try {

            Optional<User> user = userRepository.findById(id);

            if (user.isPresent() == false) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.OK,
                                null,
                                "Nasabah tidak ditemukan"),
                        HttpStatus.OK);

            }

            ListUserDto userDto = new ListUserDto();

            userDto.setId(user.get().getId());
            userDto.setUsername(user.get().getUsername());
            userDto.setEmail(user.get().getEmail());
            userDto.setNama_lengkap(user.get().getNamaLengkap());
            userDto.setJenis_kelamin(user.get().getJenisKelamin());
            userDto.setTgl_lahir(user.get().getTglLahir());
            userDto.setActive(user.get().isActive());
            userDto.setNo_hp(user.get().getNoHp());
            userDto.setNo_ktp(user.get().getNoKtp());
            userDto.setNo_rekening(user.get().getNoRekening());
            userDto.setFotoKtp(user.get().getFotoKtp());
            userDto.setSelfieKtp(user.get().getSelfieKtp());

            List<String> tagList = new ArrayList<>();

            tagList.add(user.get().getStatus());

            userDto.setTags(tagList);


            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            userDto,
                            "Data nasabah berhasil dimuat"),
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
    public ResponseEntity<?> acceptUserAccount(Long id, String status) {
        try {

            Optional<User> user = userRepository.findById(id);

            if (user.isPresent() == false) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.OK,
                                null,
                                "Nasabah tidak ditemukan"),
                        HttpStatus.OK);
            }

            User existUser = user.get();
            String message = "";

            if (status.equals("confirm")) {
                existUser.setAccepted(true);
                existUser.setStatus("Diterima");
                userRepository.save(existUser);
                message = "disetujui";
            } else {
                existUser.setAccepted(false);
                existUser.setStatus("Ditolak");
                userRepository.save(existUser);
                message = "ditolak";
            }

            ListUserDto userDto = new ListUserDto();

            userDto.setId(user.get().getId());
            userDto.setUsername(user.get().getUsername());
            userDto.setEmail(user.get().getEmail());
            userDto.setNama_lengkap(user.get().getNamaLengkap());
            userDto.setJenis_kelamin(user.get().getJenisKelamin());
            userDto.setTgl_lahir(user.get().getTglLahir());
            userDto.setActive(user.get().isActive());
            userDto.setNo_hp(user.get().getNoHp());
            userDto.setNo_ktp(user.get().getNoKtp());
            userDto.setNo_rekening(user.get().getNoRekening());
            userDto.setFotoKtp(user.get().getFotoKtp());
            userDto.setSelfieKtp(user.get().getSelfieKtp());

            List<String> tagList = new ArrayList<>();

            tagList.add(user.get().getStatus());

            userDto.setTags(tagList);

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            userDto,
                            "Profil nasabah berhasil " + message),
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
    public ResponseEntity<?> getNewUserNotif() {
        try {

            List<User> users = userRepository.findByStatus("Pending");
            List<NotifNewUserDto> newUserDto = new ArrayList<>();

            for (User user : users) {
                NotifNewUserDto notifNewUserDto = modelMapperUtil
                        .modelMapperInit()
                        .map(user, NotifNewUserDto.class);

                notifNewUserDto.setId(user.getId());
                notifNewUserDto.setAvatar("https://res.cloudinary.com/dewaqintoro/image/upload/v1600011816/katak/icon-list_hrdkkn.png");
                notifNewUserDto.setNamaLengkap(user.getNamaLengkap());
                notifNewUserDto.setJudul("Menunggu Persetujuan");
                notifNewUserDto.setDeskripsi("Menunggu persetujuan pembuatan akun baru");

                newUserDto.add(notifNewUserDto);
            }

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            newUserDto,
                            "Notifikasi succes"),
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
