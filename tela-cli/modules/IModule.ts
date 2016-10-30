export interface IModule {

    readonly name: string;
    isConfigured(): boolean;
    connect(callback: (accessToken: string) => void): void;

}
