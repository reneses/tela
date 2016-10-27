package io.reneses.tela.modules.instagram.models;


/**
 * MediaResource class.
 */
public class MediaResource {

    private String url, code;
    private int width, height;

    /**
     * Getter for the field <code>url</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the field <code>url</code>.
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for the field <code>code</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter for the field <code>code</code>.
     *
     * @param code a {@link java.lang.String} object.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter for the field <code>width</code>.
     *
     * @return a int.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setter for the field <code>width</code>.
     *
     * @param width a int.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter for the field <code>height</code>.
     *
     * @return a int.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter for the field <code>height</code>.
     *
     * @param height a int.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaResource that = (MediaResource) o;

        if (width != that.width) return false;
        if (height != that.height) return false;
        return url.equals(that.url);

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}
