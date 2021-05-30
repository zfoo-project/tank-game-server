module.exports = {
    devServer: {
        disableHostCheck: true,
        proxy: {
            '/proxy': {
                target: 'http://127.0.0.1:9600/',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/proxy': ''
                }
            }
        }
    },

    publicPath: process.env.NODE_ENV === 'production' ? process.env.VUE_APP_STATIC_URL : '/',

    configureWebpack: {
        resolve: {
            symlinks: false
        }
    },

    pwa: {
        workboxOptions: {
            skipWaiting: true
        }
    },

    productionSourceMap: false,

    transpileDependencies: ['vuetify']
};
