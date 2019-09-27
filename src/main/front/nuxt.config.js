const webpack = require('webpack');
console.log(webpack);
module.exports = {
    head: {
        script: [
            { src:'/vendors/js/vendors.min.js'},
            { src:'/js/core/app-menu.js'},
            { src:'/js/core/app.js'},
        ],
        link: [
            {rel: 'apple-touch-icon', href: '/images/ico/apple-icon-120.png'},
            {rel: 'shortcut icon', href: '/images/ico/favicon.ico'},
            {
                rel: 'stylesheet',
                href: 'https://fonts.googleapis.com/css?family=Muli:300,300i,400,400i,600,600i,700,700i%7CComfortaa:300,400,700'
            },
            {
                rel: 'stylesheet',
                href: 'https://maxcdn.icons8.com/fonts/line-awesome/1.1/css/line-awesome.min.css'
            },
            {rel: 'stylesheet', href: '/vendors/css/charts/chartist.css'},
            {rel: 'stylesheet', href: '/vendors/css/charts/chartist-plugin-tooltip.css'},
            {rel: 'stylesheet', href: '/css/vendors.css'},
            {rel: 'stylesheet', href: '/css/app.css'},
            {rel: 'stylesheet', href: '/css/core/menu/menu-types/vertical-menu.css'},
            {rel: 'stylesheet', href: '/css/core/colors/palette-gradient.css'},
            {rel: 'stylesheet', href: '/css/pages/chat-application.css'},
            {rel: 'stylesheet', href: '/css/pages/dashboard-analytics.css'},
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
        ]
    },
    modules: [
        '@nuxtjs/axios',
        'bootstrap-vue/nuxt'
    ],
    bootstrapVue: {
        bootstrapCSS: false, // Or `css: false`
        // bootstrapVueCSS: false // Or `bvCSS: false`
    },
    plugins: [
        '~/plugins/axios',
        '~/plugins/eventBus'
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
        middleware: 'auth'
    }
}