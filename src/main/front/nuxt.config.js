const webpack = require('webpack');
console.log(webpack);
module.exports = {
    build: {
        extend(config, ctx) {
            if (ctx.isDev) {
                config.devtool = ctx.isClient ? 'source-map' : 'inline-source-map'
            }
        },
        plugins: [
            new webpack.ProvidePlugin({
                '$': 'jquery'
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
}