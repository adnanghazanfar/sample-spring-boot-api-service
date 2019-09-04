import { Command } from "@oclif/command";
import * as logSymbols from "log-symbols";
import { readConfiguration } from "../config";
import { transferFiles } from "../files";
import { checkZowe } from "../zowe";

export default class Deploy extends Command {
    static description = "deploy the API service artifacts to z/OS";

    async run() {
        const [userConfig, projectConfig] = readConfiguration(this);

        checkZowe(this);

        if (!projectConfig.deployment.files) {
            this.warn("Nothing to deploy");
        } else {
            transferFiles(projectConfig.deployment.files, userConfig.zosTargetDir, userConfig, this);
            this.log(logSymbols.success, "Deployment to z/OS completed");
            this.log(
                logSymbols.info,
                "Use 'zowe-api-dev config' to configure your application to z/OS or 'zowe-api-dev start' to start already configured application"
            );
        }
    }
}