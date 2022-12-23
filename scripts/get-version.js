const yaml = require('js-yaml');
const fs = require('fs');

const doc = yaml.load(fs.readFileSync('target/classes/application.yml', 'utf8'));
console.log(doc.application.version);