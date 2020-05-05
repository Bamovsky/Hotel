package ua.nure.cherkashyn.hotel.db.entity;


/**
 * Role entity.
 *
 * @author Vladimir Cherkashyn
 */
public enum Role {
    CLIENT, MANAGER;


    public static Role getRole(User user) {
        int roleId = (int) user.getRoleId();
        return Role.values()[roleId];
    }


    /**
     * Get name in lower case.
     */
    public String getName() {
        return name().toLowerCase();
    }
}
