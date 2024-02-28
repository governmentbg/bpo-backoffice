module.exports = function(grunt) {

    let concatFile = 'materialize/temp/js/materialize_concat.js.map';

    // configure the tasks
    let config = {

        //  Sass
        sass: {
            // Task
            expanded: {
                // Target options
                options: {
                    outputStyle: 'expanded',
                    sourceMap: false
                },
                files: {
                    'src/main/webapp/resources/css/materialize.css': 'materialize/sass/materialize.scss'
                }
            },

            min: {
                options: {
                    outputStyle: 'compressed',
                    sourceMap: false
                },
                files: {
                    'src/main/webapp/resources/css/materialize.css': 'materialize/sass/materialize.scss'
                }
            },

            // Compile bin css
            bin: {
                options: {
                    style: 'expanded',
                    sourceMap: 'none'
                },
                files: {
                    'src/main/webapp/resources/css/materialize.css': 'materialize/sass/materialize.scss'
                }
            }
        },

        // PostCss Autoprefixer
        postcss: {
            options: {
                processors: [
                    require('autoprefixer')({
                        browsers: [
                            'last 2 versions',
                            'Chrome >= 30',
                            'Firefox >= 30',
                            'ie >= 10',
                            'Safari >= 8'
                        ]
                    })
                ]
            },
            expanded: {
                src: 'src/main/webapp/resources/css/materialize.css'
            },
            min: {
                src: 'src/main/webapp/resources/css/materialize.min.css'
            },
            bin: {
                src: 'src/main/webapp/resources/css/materialize.css'
            }
        },

        babel: {
            options: {
                sourceMap: false,
                plugins: [
                    'transform-es2015-arrow-functions',
                    'transform-es2015-block-scoping',
                    'transform-es2015-classes',
                    'transform-es2015-template-literals',
                    'transform-es2015-object-super'
                ]
            },
            bin: {
                options: {
                    sourceMap: true
                },
                files: {
                    'src/main/webapp/resources/js/lib/materialize.js': 'materialize/temp/js/materialize_concat.js'
                }
            },
            dist: {
                files: {
                    'src/main/webapp/resources/js/lib/materialize.js': 'materialize/temp/js/materialize.js'
                }
            }
        },

        //  Concat
        concat: {
            options: {
                separator: ';'
            },
            dist: {
                // the files to concatenate
                src: [
                    'materialize/js/cash.js',
                    'materialize/js/component.js',
                    'materialize/js/global.js',
                    'materialize/js/anime.min.js',
                    'materialize/js/dropdown.js',
                    'materialize/js/forms.js',
                    'materialize/js/characterCounter.js',
                    'materialize/js/modal.js',
                    'materialize/js/materialbox.js',
                    'materialize/js/waves.js',
                    'materialize/js/buttons.js',
                    'materialize/js/collapsible.js',
                    'materialize/js/select.js'
                ],
                // the location of the resulting JS file
                dest: 'materialize/temp/js/materialize.js'
            },
            bin: {
                // the files to concatenate
                options: {
                    sourceMap: true,
                    sourceMapStyle: 'link'
                },
                src: [
                    'materialize/js/cash.js',
                    'materialize/js/component.js',
                    'materialize/js/global.js',
                    'materialize/js/anime.min.js',
                    'materialize/js/dropdown.js',
                    'materialize/js/forms.js',
                    'materialize/js/characterCounter.js',
                    'materialize/js/modal.js',
                    'materialize/js/materialbox.js',
                    'materialize/js/waves.js',
                    'materialize/js/buttons.js',
                    'materialize/js/chips.js',
                    'materialize/js/collapsible.js',
                    'materialize/js/select.js'
                ],
                // the location of the resulting JS file
                dest: 'materialize/temp/js/materialize_concat.js'
            }
        },

        //  Uglify
        uglify: {
            options: {
                // Use these options when debugging
                // mangle: false,
                // compress: false,
                // beautify: true
            },
            dist: {
                files: {
                    'src/main/webapp/resources/js/lib/materialize.js': ['src/main/webapp/resources/js/lib/materialize.js']
                }
            },
            bin: {
                files: {
                    'src/main/webapp/resources/js/lib/materialize.js': ['src/main/webapp/resources/js/lib/materialize.js']
                }
            }
        },

        //  Clean
        clean: {
            temp: {
                src: ['materialize/temp/', '/catalina.*']
            }
        },

        //  Watch Files
        watch: {

            js: {
                files: ['materialize/js/**/*'],
                tasks: ['js_compile'],
                options: {
                    interrupt: false,
                    spawn: false
                }
            },

            sass: {
                files: ['sass/**/*'],
                tasks: ['sass_compile'],
                options: {
                    interrupt: false,
                    spawn: false
                }
            }
        },

        //  Concurrent
        concurrent: {
            options: {
                logConcurrentOutput: true,
                limit: 10
            },
            monitor: {
                tasks: [
                    'sass_compile',
                    'js_compile',
                    'watch:js',
                    'watch:sass',
                    'notify:watching',
                ]
            }
        },

        //  Notifications
        notify: {
            watching: {
                options: {
                    enabled: true,
                    message: 'Watching Files!',
                    title: 'Materialize', // defaults to the name in package.json, or will use project directory's name
                    success: true, // whether successful grunt executions should be notified automatically
                    duration: 1 // the duration of notification in seconds, for `notify-send only
                }
            },

            sass_compile: {
                options: {
                    enabled: true,
                    message: 'Sass Compiled!',
                    title: 'Materialize',
                    success: true,
                    duration: 1
                }
            },

            js_compile: {
                options: {
                    enabled: true,
                    message: 'JS Compiled!',
                    title: 'Materialize',
                    success: true,
                    duration: 1
                }
            }
        },

    };


    grunt.initConfig(config);

    // load the tasks
    // grunt.loadNpmTasks('grunt-gitinfo');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-compress');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-notify');
    grunt.loadNpmTasks('grunt-browser-sync');
    grunt.loadNpmTasks('grunt-postcss');
    grunt.loadNpmTasks('grunt-babel');

    // define the tasks
    grunt.registerTask('release', [
        'sass:min',
        'postcss:min',
        'concat:dist',
        'babel:dist',
        'uglify:dist',
        'clean:temp'
    ]);

    grunt.task.registerTask('configureBabel', 'configures babel options', function() {
        config.babel.bin.options.inputSourceMap = grunt.file.readJSON(concatFile);
    });

    grunt.registerTask('js_compile', ['concat:bin', 'configureBabel', 'babel:bin', 'clean:temp']);
    grunt.registerTask('sass_compile', ['sass:bin', 'postcss:bin', 'notify:sass_compile']);
    grunt.registerTask('monitor', ['concurrent:monitor']);
};
