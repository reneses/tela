#!/usr/bin/env node

import {TelaCli} from "../TelaCli";
import {ConnectionManager} from "../ConnectionManager";

/**
 * Execute the input
 *
 * @param telaCli
 * @param command
 * @param args
 */
function executeAuthorized(telaCli: TelaCli, command: string, args: any[]) {

    // Retrieve the connection and check that it is actually connected
    let connection = new ConnectionManager().load();
    if (!connection.isConnected()) {
        console.error('A Tela connection is required. Connect with: connect [url] [port]');
        return;
    }

    // Execute actions
    switch (command.toLowerCase()) {
        case 'configure':
            telaCli.configure(args[0], args[1], args[2]);
            break;
        case 'disconnect':
            telaCli.disconnect();
            break;
        case 'link':
            telaCli.linkModule(args[0]);
            break;
        case 'unlink':
            telaCli.unlinkModule(args[0]);
            break;
        case 'execute':
            telaCli.executeAction(args[0], args[1], args.slice(2));
            break;
        case 'help':
            telaCli.help(args[0]);
            break;
        default:
            console.error('Command not found');
            TelaCli.printCommands();
    }

}

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
        case '--help':
        case '-h':
            TelaCli.printCommands();
            break;

        // Connect
        case 'connect':
            telaCli.connect(args[0], args[1]);
            break;

        // Connected actions
        default:
            executeAuthorized(telaCli, command, args);

    }

}

//------------------------------------ MAIN METHOD ------------------------------------//
let telaCli = new TelaCli();
var args = process.argv.slice(2);
let command = args[0];
if (!command) {
    console.error('No argument supplied!');
    TelaCli.printCommands();
}
else {
    execute(telaCli, command, args.slice(1));
}