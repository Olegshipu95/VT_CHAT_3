package user.dto.response;

import user.entity.Role;

import java.util.List;
import java.util.UUID;

public record AuthorizationDetails(
    UUID id,
    String username,
    List<Role> roles
) { }