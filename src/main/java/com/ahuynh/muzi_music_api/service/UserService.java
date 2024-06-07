package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.role.Role;
import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.request.SignUpRequest;
import com.ahuynh.muzi_music_api.payload.request.UserRequest;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final FirebaseService firebaseService;
    private final SongRepository songRepository;


    public User saveNewUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException("Email already exists");
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new CustomException("Username already exists");
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        Set<Role> roles = new HashSet<>();
        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));

        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
        }
        User user = new User(signUpRequest.getEmail(), encodedPassword, signUpRequest.getUsername(), roles);

        return userRepository.save(user);
    }

    public User save(String email, String password, String username, MultipartFile avatar, boolean enable) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException("Email already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new CustomException("Username already exists");
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(password);
        Set<Role> roles = new HashSet<>();
        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));

        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
        }
        String url = firebaseService.upload(avatar, "image/png");
        User user = new User(email, encodedPassword, username, roles, url, enable);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not exits id =" + id));
        userRepository.deleteById(id);
    }

    public User updateUser(Long iid, UserRequest userRequest) {
        User updateUser = userRepository.findById(iid).orElseThrow(() ->
                new ResourceNotFoundException("User not exits id =" + iid));

        if (userRequest.getEmail() != null) {

            updateUser.setEmail(userRequest.getEmail());
        }
        if (userRequest.getUsername() != null) {

            updateUser.setUsername(userRequest.getUsername());
        }

        if (userRequest.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(userRequest.getPassword());

            updateUser.setPassword(encodedPassword);
        }


        return userRepository.save(updateUser);
    }

    public User updateEnable(Long id, boolean b) {
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not exits id =" + id));
        updateUser.setEnabled(b);
        return userRepository.save(updateUser);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not exits id =" + id));
    }

    @Transactional
    public void followUser(Long followerId, Long followedId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Followed user not found"));
        follower.follow(followed);
        userRepository.save(follower);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followedId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Followed user not found"));
        follower.unfollow(followed);
        userRepository.save(follower);
    }

    public List<User> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
        System.out.println(user.getFollowing());
        return userRepository.findFollowingById(userId);
    }

    public List<User> getFollower(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
        return userRepository.findFollowerById(userId);
    }

    public void loveSong(Long userId, Long songId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found " + songId));
        user.addLovedSong(song);
        userRepository.save(user);
    }

    public void unloveSong(Long userId, Long songId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found " + songId));
        user.removeLovedSong(song);
        userRepository.save(user);
    }

    public Set<Song> getLoveSong(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        return user.getLoveSongs();

    }

    public boolean isLoveSong(Long id, Long songId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found " + songId));
        return user.getLoveSongs().contains(song);

    }

    public User editAvatar(Long id, MultipartFile avatar) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        String url = firebaseService.upload(avatar, "image/png");
        user.setAvatar(url);
        return userRepository.save(user);
    }

    public List<Playlist> getAllPlaylistById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        return userRepository.findPlaylistById(id);
    }

    public User getUserByUserName(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + username));
    }


    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + email));
    }


    public User editPassword(Long id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
}
