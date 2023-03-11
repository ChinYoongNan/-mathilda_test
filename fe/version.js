const { gitDescribeSync } = require('git-describe');
const { version } = require('./package.json');
const { resolve, relative } = require('path');
const { writeFileSync } = require('fs-extra');

const args = process.argv.slice(2);
let gitInfo = {};
if (!args.length) {
    gitInfo = gitDescribeSync({
        dirtyMark: false,
        dirtySemver: false
    });
} else {
    gitInfo = {
        hash: args[0],
        raw: args[0]
    }
}
gitInfo.version = version;

const file = resolve(__dirname, 'src', 'environments', 'version.ts');
writeFileSync(file,
    `// IMPORTANT: THIS FILE IS AUTO GENERATED! DO NOT MANUALLY EDIT OR CHECKIN!
/* tslint:disable */
export const VERSION = ${JSON.stringify(gitInfo, null, 4)};
/* tslint:enable */
`, { encoding: 'utf-8' });

console.log(`Wrote version info ${gitInfo.raw} to ${relative(resolve(__dirname, '..'), file)}`);
