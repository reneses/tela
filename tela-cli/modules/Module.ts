export interface Module {

    readonly name: string;
    isConfigured(): boolean;
    connect(callback: (accessToken: string) => void): void;

}
