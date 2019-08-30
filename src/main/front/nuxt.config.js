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
    ],
    // axios: {
    //     proxy: true     // proxy 사용
    // },
    // proxy: {
    //     'http://localhost:3000/': 'http://localhost:8080/'    // proxy url
    // }
}