package com.api.crudSinAyuda.services;

import com.api.crudSinAyuda.exceptions.EmailAlreadyExistsException;
import com.api.crudSinAyuda.exceptions.UserNotFoundException;
import com.api.crudSinAyuda.models.UserModel;
import com.api.crudSinAyuda.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setId(1L);
        user.setFirstName("Juan");
        user.setLastName("Pérez");
        user.setEmail("juan@example.com");
        user.setPhone("123456789");
    }

    // Convención de nombres: metodo_escenario_resultadoEsperado
    @Test
    void getUsers_always_returnsListOfUsers(){
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserModel> result = userService.getUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("juan@example.com");
    }

    @Test
    void saveUser_whenEmailIsNew_saveAndReturnUser(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        UserModel resul = userService.saveUser(user);

        assertThat(resul.getFirstName()).isEqualTo("Juan");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUser_whenEmailAlreadyExists_throwsEmailAlreadyExistsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.saveUser(user))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("El email ya está registrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_whenUserExists_returnsOptionalWithUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserModel> result = userService.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void getById_whenUserDoesNotExist_returnsEmptyOptional() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserModel> result = userService.getById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void updateById_whenUserExistsAndEmailIsFree_updatesAndReturnsUser() {
        UserModel request = new UserModel();
        request.setFirstName("Carlos");
        request.setLastName("López");
        request.setEmail("carlos@example.com");
        request.setPhone("300099999");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("carlos@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        UserModel result = userService.updateById(request, 1L);

        assertThat(result.getFirstName()).isEqualTo("Carlos");
        verify(userRepository).save(user);
    }

    @Test
    void updateById_whenUserDoesNotExists_throwsUserNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateById(user, 99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateById_whenEmailBelongsToAnotherUser_throwsEmailAlreadyExistsException() {

        UserModel otherUser = new UserModel();
        otherUser.setId(2L);
        otherUser.setEmail("juan@example.com");

        UserModel request = new UserModel();
        request.setEmail("juan@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> userService.updateById(request, 1L))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void deleteById_whenUserExists_returnsTrueAndDeleteUser() {

        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.deleteById(1L);

        assertThat(result).isTrue();
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteByIdWhenUserDoesNotExists_returnsFalse() {

        when(userRepository.existsById(99L)).thenReturn(false);

        boolean result = userService.deleteById(99L);

        assertThat(result).isFalse();
        verify(userRepository, never()).deleteById(any());
    }
}
