export class User {

    public id: number;
    public username: string;
    public fullName: string;
    public picture: string;

    public bio: string;
    public website: string;
    public followers: number;
    public following: number;
    public media: number;

    constructor(obj: { id: number, username: string, fullName: string, picture: string,
        bio?: string, website?: string, followers?: number; following?: number, media?: number}) {
        this.id = obj.id;
        this.username = obj.username;
        this.fullName = obj.fullName;
        this.picture = obj.picture;

        this.bio = obj.bio;
        this.website = obj.website;
        this.followers = obj.followers;
        this.following = obj.following;
        this.media = obj.media;
    }

}