export const appConstant = {
    appVersion: 'v1.0.0',
    appName: 'zfoo',
    // appName: 'aaa',
    appIcon: 'mdi-alpha-z-circle',

    homeUrl: process.env.VUE_APP_HTTP

};

export const settingConstant = {
    theme: {
        dark: 0,
        light: 1
    },
    language: {
        cn: {
            type: 0,
            value: 'cn'
        },
        en: {
            type: 1,
            value: 'en'
        }
    }
};


