#!/usr/bin/env node

import {TelaCli} from "../TelaCli";

/**
 * Execute the input
 *
 * @param telaCli
 * @param command
 * @param args
 */
function execute(telaCli: TelaCli, command: string, args: any[]) {

    switch (command.toLowerCase()) {

        // Help
        case "--help":
        case "-h":
            telaCli.printCommands();
            break;

        // Connect
        case "connect":
            telaCli.connect(args[0], args[1]);
            break;

        // Connected actions
        case "configure":
            telaCli.configure(args[0], args[1], args[2]);
            break;
        case "disconnect":
            telaCli.disconnect();
            break;
        case "link":
            telaCli.linkModule(args[0]);
            break;
        case "unlink":
            telaCli.unlinkModule(args[0]);
            break;
        case "execute":
            telaCli.execute(args[0], args[1], args.slice(2));
            break;
        case "help":
            telaCli.help(args[0]);
            break;
        case "schedule":
            telaCli.schedule(args[0], args[1], args[2], args.slice(3));
            break;
        case "scheduled":
            telaCli.getScheduled();
            break;
        case "cancel":
            telaCli.cancelScheduled(args[0]);
            break;
        default:
            console.error("Command not found");
            telaCli.printCommands();

    }

}

// ------------------------------------ MAIN METHOD ------------------------------------ //
let telaCli = new TelaCli();
let args = process.argv.slice(2);
let command = args[0];
if (!command) {
    console.error("No argument supplied!");
    telaCli.printCommands();
} else {
    execute(telaCli, command, args.slice(1));
}
