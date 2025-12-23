package com.language_practice_server.server_demo.db.adapter;

import com.language_practice_server.server_demo.db.entity.UserEntity;
import com.language_practice_server.server_demo.db.repository.UserRepositoryJpa;
import com.language_practice_server.server_demo.domain.model.User;
import com.language_practice_server.server_demo.domain.repository.UserRepository;
import com.language_practice_server.server_demo.mapper.PersonMapper;
import com.language_practice_server.server_demo.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserJpaAdapter implements UserRepository {
    private final UserRepositoryJpa userRepositoryJpa;
    private final PersonJpaAdapter personJpaAdapter;
    private final UserMapper userMapper;
    private final PersonMapper personMapper;

    public UserJpaAdapter(UserRepositoryJpa userRepositoryJpa,
                          PersonJpaAdapter personJpaAdapter,
                          UserMapper userMapper, PersonMapper personMapper) {
        this.userRepositoryJpa = userRepositoryJpa;
        this.personJpaAdapter = personJpaAdapter;
        this.userMapper = userMapper;
        this.personMapper = personMapper;
    }

    @Override
    public Optional<User> findUserById(Long id) {
//        Optional<UserEntity> existingUser = userRepositoryJpa.findById(id);
//        if(existingUser.isEmpty()){
//            return Optional.empty();
//        }
//        UserEntity user= existingUser.get();
//        if(user.getPerson().getId()==null){
//
//        }
        return userRepositoryJpa.findById(id).map(userMapper::toModel);
    }

    @Override
    public Optional<User> findUserByUserName(String userName) {
        //todo this should be checked on the Service level with exception throw
//        if (userName == null) {
//            return Optional.empty();
//        }

        return userRepositoryJpa.findByUserName(userName).map(userMapper::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryJpa.findByEmail(email).map(userMapper::toModel);
    }

    @Override
    public User saveUser(User user) {
        UserEntity savedUserEntity = userRepositoryJpa.save(userMapper.toEntity(user));
        return userMapper.toModel(savedUserEntity);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepositoryJpa.deleteById(id);
    }
}
