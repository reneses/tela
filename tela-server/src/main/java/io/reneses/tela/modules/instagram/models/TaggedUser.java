package io.reneses.tela.modules.instagram.models;


/**
 * TaggedUser class.
 */
public class TaggedUser {

    private Position position;
    private User user;

    /**
     * Getter for the field <code>position</code>.
     *
     * @return a {@link Position} object.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Setter for the field <code>position</code>.
     *
     * @param position a {@link Position} object.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Getter for the field <code>user</code>.
     *
     * @return a {@link User} object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the field <code>user</code>.
     *
     * @param user a {@link User} object.
     */
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaggedUser that = (TaggedUser) o;

        if (!position.equals(that.position)) return false;
        return user.equals(that.user);

    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaggedUser{" +
                "position=" + position +
                ", user=" + user +
                '}';
    }

}
