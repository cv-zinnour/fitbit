package ca.uqtr.fitbit.entity.fitbit;

import ca.uqtr.fitbit.entity.BaseEntity;
import ca.uqtr.fitbit.entity.Device;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Auth", schema = "public")
public class Auth extends BaseEntity {

    @Column(name = "authorization_code")
    private String authorizationCode;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expired_token")
    private boolean expiredToken;

    @Column(name = "expires_in")
    private int expiresIn;

    @Column(name = "scope")
    private String scope;

    @Column(name = "token_type")
    private String tokenType;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Device device;

    public Auth(String authorizationCode, String accessToken, String refreshToken, boolean expiredToken, int expiresIn, String scope, String tokenType) {
        this.authorizationCode = authorizationCode;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredToken = expiredToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public void setAccessTokenRefreshToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Auth() {
    }
}
