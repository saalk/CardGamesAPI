// The first part is the "wrapper" function, which encapsulates your Grunt configuration
module.exports = function(grunt) {

  // Within that function we can initialize our configuration object:
  grunt.initConfig({
    // Next, we can store the project settings from the package.json file into the pkg property.
    // This allows us to refer to the values of properties within our package.json file
    pkg: grunt.file.readJSON('package.json'),
    // Now we can define a configuration for each of the tasks we mentioned.
    concat: {
      options: {
      // define a string to put between each file in the concatenated output
        separator: ';'
      },
      dist: {
        // the files to concatenate
        src: ['src/**/*.js'],
        // the location of the resulting JS file
        // this refer to the name property that's in the JSON file with pkg.name
        dest: 'dist/<%= pkg.name %>.js'
      }
    },
    // configure the grunt-contrib-uglify plugin which minifies the JavaScript code:
    uglify: {
      options: {
        // the banner is inserted at the top of the output
        banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
      },
      dist: {
        files: {
          'dist/<%= pkg.name %>.min.js': ['<%= concat.dist.dest %>']
        }
      }
    },
    // automate the testing so give the location of the test runner files
    qunit: {
      files: ['test/**/*.html']
    },
    jshint: {
      // define the files to lint
      files: ['Gruntfile.js', 'src/**/*.js', 'test/**/*.js'],
      // configure JSHint (documented at http://www.jshint.com/docs/)
      options: {
        // more options here if you want to override JSHint defaults
        globals: {
          jQuery: true,
          console: true,
          module: true,
          document: true
        }
      }
    },
    // Turning the previous description into a configuration for
    // grunt-contrib-watch results in the snippet below:
    watch: {
      files: ['<%= jshint.files %>'],
      tasks: ['jshint', 'qunit']
    }
  });
    // The last step to perform is to load in the Grunt plugins we need.
    // All of these should have been previously installed through npm.
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');
    // this would be run by typing "grunt test" on the command line
    grunt.registerTask('test', ['jshint', 'qunit']);
    // the default task can be run just by typing "grunt" on the command line
    grunt.registerTask('default', ['jshint', 'qunit', 'concat', 'uglify']);
};