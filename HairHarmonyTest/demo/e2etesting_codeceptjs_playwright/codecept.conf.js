/** @type {CodeceptJS.MainConfig} */
exports.config = {
  tests: './*.js',
  output: './output',
  helpers: {
    Playwright: {
      browser: 'chromium',
      url: 'http://localhost',
      show: true
    }
  },
  include: {
    I: './steps_file.js'
  },
  name: 'e2etesting_codeceptjs_playwright'
}