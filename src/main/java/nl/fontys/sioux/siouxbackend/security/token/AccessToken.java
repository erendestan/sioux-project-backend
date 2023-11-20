package nl.fontys.sioux.siouxbackend.security.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.fontys.sioux.siouxbackend.domain.Position;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@Builder
public class AccessToken {
    private final String subject;
    private final Long userId;
    private final Position role;
}