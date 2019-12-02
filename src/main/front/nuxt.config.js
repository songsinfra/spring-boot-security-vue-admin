const webpack = require('webpack');
console.log("webpack config :" , process.env.npm_lifecycle_event);
module.exports = {
    head: {
        script: [
             {src: '/js/core/libraries/jquery.min.js'}

        ],
        link: [
            {rel: 'apple-touch-icon', href: '/images/ico/apple-icon-120.png'},
            {rel: 'shortcut icon', href: '/images/ico/favicon.ico'},
            {
                rel: 'stylesheet', href: '/fonts/simple-line-icons/style.css'
            },
            {
                rel: 'stylesheet',
                href: '/fonts/line-awesome/css/line-awesome.css'
            },
            {rel: 'stylesheet', href: '/vendors/css/charts/chartist.css'},
            {rel: 'stylesheet', href: '/vendors/css/charts/chartist-plugin-tooltip.css'},
            {rel: 'stylesheet', href: '/css/vendors.css'},
            {rel: 'stylesheet', href: '/css/app.css'},
            {rel: 'stylesheet', href: '/css/core/menu/menu-types/vertical-menu.css'},
            {rel: 'stylesheet', href: '/css/core/colors/palette-gradient.css'},
        ],
    },
    build: {
        extend(config, ctx) {
            if (ctx.isDev) {
                config.devtool = ctx.isClient ? 'source-map' : 'inline-source-map'
            }
        },
        plugins: [
            new webpack.ProvidePlugin({
                '$': 'jquery',
                'jQuery$': 'jquery',
            })
        ],
        babel: {
            presets({ isServer }) {
                const targets = isServer ? { node: 'current' } : { ie: 11 }
                return [
                    [
                        require.resolve('@nuxt/babel-preset-app'),
                        {
                            buildTarget: isServer ? 'server' : 'client',
                            targets
                        }
                    ]
                ]
            }
        }
    },
    env: {
        contextPath: process.env.npm_lifecycle_event === 'dev' ? '/api' : ""
    },
    generate: {
        dir: "../resources/static",
        // subFolders : false
        // devtools: true,
    },
    modules: [
        '@nuxtjs/axios',
        // 'nuxt-polyfill',
        'bootstrap-vue/nuxt',
        '@nuxtjs/moment',
        ['nuxt-validate', {
            lang: 'ko',
            fieldsBagName: 'veeFields'
        }]
    ],
    bootstrapVue: {
        bootstrapCSS: false, // Or `css: false`
        // bootstrapVueCSS: false // Or `bvCSS: false`
    },
    plugins: [
        '~/plugins/axios',
        '~/plugins/eventBus',
        '~/plugins/jsModal',
    ],
    axios: {
        proxyHeaders: true,
        credentials: true,
        proxy: true     // proxy 사용
    },
    proxy: {
        '/api/': {
            target: 'http://localhost:8080/api',
            pathRewrite: {'^/api': ''},
        }    // proxy url
    },
    router:{
        middleware: 'auth',
        // mode: 'hash'
    },

    // polyfill: {
    //     features: [
    //         /*
    //             Feature without detect:
    //
    //             Note:
    //               This is not recommended for most polyfills
    //               because the polyfill will always be loaded, parsed and executed.
    //         */
    //         {
    //             require: 'url-polyfill' // NPM package or require path of file
    //         },
    //
    //         /*
    //             Feature with detect:
    //
    //             Detection is better because the polyfill will not be
    //             loaded, parsed and executed if it's not necessary.
    //         */
    //         {
    //             require: 'intersection-observer',
    //             detect: () => 'IntersectionObserver' in window,
    //         },
    //
    //         /*
    //             Feature with detect & install:
    //
    //             Some polyfills require a installation step
    //             Hence you could supply a install function which accepts the require result
    //         */
    //         {
    //             require: 'smoothscroll-polyfill',
    //
    //             // Detection found in source: https://github.com/iamdustan/smoothscroll/blob/master/src/smoothscroll.js
    //             detect: () => 'scrollBehavior' in document.documentElement.style && window.__forceSmoothScrollPolyfill__ !== true,
    //
    //             // Optional install function called client side after the package is required:
    //             install: (smoothscroll) => smoothscroll.polyfill()
    //         }
    //     ]
    // },
}