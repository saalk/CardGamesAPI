module.exports = function(config){
  config.set({

    basePath : '../../../',

    files : [
      'src/main/webapp/vendor/angular**/**.min.js',
      'src/main/webapp/vendor/angular-mocks/angular-mocks.js',
      'src/main/webapp/javascript/**/*.js',
      'src/test/javascript/unit/**/*.js'
    ],
    // files to exclude
    exclude : [
        'app/lib/angular/angular-loader.js'
      , 'app/lib/angular/*.min.js'
      , 'app/lib/angular/angular-scenario.js'
    ],

    // karma has its own autoWatch feature but Grunt watch can also do this
    autoWatch : false,

    // testing framework, be sure to install the correct karma plugin
    frameworks: ['jasmine'],

    // browsers to test against, be sure to install the correct browser launcher plugins
    browsers : ['PhantomJS'],

    // map of preprocessors that is used mostly for plugins
    preprocessors: {
      // 'app/partials/directives/*.html': 'html2js',
      // 'app/partials/*.html': 'html2js'

      // test coverage
      'app/js/controllers/*.js': ['jshint', 'coverage'],
      'app/js/directives/*.js': ['jshint', 'coverage'],
      'app/js/app.js': ['jshint', 'coverage']
    },

    reporters: ['progress', 'coverage'],

    plugins : [
          'karma-jshint-preprocessor',
          'karma-coverage',
          'karma-chrome-launcher',
          'karma-firefox-launcher',
          'karma-jasmine',
          'karma-ng-html2js-preprocessor',
          'karma-phantomjs-launcher'
          'karma-junit-reporter'
    ],
    // plugin settings
    ngHtml2JsPreprocessor: {
      stripPrefix: 'app/'
    },
    coverageReporter: {
      // type of file to output, use text to output to console
      type : 'text',
      // directory where coverage results are saved
      dir: 'test-results/coverage/'
      // if type is text or text-summary, you can set the file name
      // file: 'coverage.txt'
    },
    junitReporter : {
      outputFile: 'target/test_out/junit-results.xml',
      suite: 'src/test/javascript/unit'
    }

  });
};