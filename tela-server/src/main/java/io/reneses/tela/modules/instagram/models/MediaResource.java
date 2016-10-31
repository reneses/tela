package io.reneses.tela.modules.instagram.models;


/**
 * MediaResource class.
 */
public class MediaResource {

    private String url, size;
    private int width, height;

    /**
     * Getter for the field <size>url</size>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the field <size>url</size>.
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for the field <size>size</size>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSize() {
        return size;
    }

    /**
     * Setter for the field <size>size</size>.
     *
     * @param size a {@link java.lang.String} object.
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Getter for the field <size>width</size>.
     *
     * @return a int.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setter for the field <size>width</size>.
     *
     * @param width a int.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter for the field <size>height</size>.
     *
     * @return a int.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter for the field <size>height</size>.
     *
     * @param height a int.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaResource that = (MediaResource) o;

        if (width != that.width) return false;
        if (height != that.height) return false;
        return url.equals(that.url);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}
