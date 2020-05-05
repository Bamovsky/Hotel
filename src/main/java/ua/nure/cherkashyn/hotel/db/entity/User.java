package ua.nure.cherkashyn.hotel.db.entity;


/**
 * User entity.
 *
 * @author Vladimir Cherkashyn
 */
public class User extends Entity {

    private static final long serialVersionUID = -5987600125387241977L;

    private String email;
    private String password;
    private boolean isApproved;
    private String approvedToken;
    private long roleId;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getApprovedToken() {
        return approvedToken;
    }

    public void setApprovedToken(String approvedToken) {
        this.approvedToken = approvedToken;
    }

}
