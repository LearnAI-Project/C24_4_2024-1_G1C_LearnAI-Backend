package pe.edu.tecsup.learnai.mapper;

import org.mapstruct.factory.Mappers;
import pe.edu.tecsup.learnai.domain.UserDTO;
import pe.edu.tecsup.learnai.entity.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    List<UserDTO> toUserDTOList(List<User> users);

    List<User> toUserList(List<UserDTO> userDTOs);

}
