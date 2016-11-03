export class Media {

    public id: string;
    public link: string;
    public caption: string;
    public createdAt: Date;
    public images: {
        (code: string): {
            url: string,
            width: number,
            height: number
        }
    };

    constructor(obj: { id: string, link: string, caption: string, createdAt: Date,
        images: {(code: string): {url: string,width: number,height: number}}}) {
        this.id = obj.id;
        this.link = obj.link;
        this.images = obj.images;
        this.caption = obj.caption;
        this.createdAt = obj.createdAt;
    }

}