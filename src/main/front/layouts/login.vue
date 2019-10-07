<template>
    <nuxt/>
</template>

<script>

    //<link rel="apple-touch-icon" href="app-assets/images/ico/apple-icon-120.png">
    // <link rel="shortcut icon" type="image/x-icon" href="app-assets/images/ico/favicon.ico">
    export default {
        name: "login",
        head() {
            return {
                link: [
                    // {rel: 'apple-touch-icon', href: '/images/ico/apple-icon-120.png'},
                    // {rel: 'shortcut icon', href: '/images/ico/favicon.ico'},
                    // {
                    //     rel: 'stylesheet',
                    //     href: 'https://fonts.googleapis.com/css?family=Muli:300,300i,400,400i,600,600i,700,700i%7CComfortaa:300,400,700'
                    // },
                    // {
                    //     rel: 'stylesheet',
                    //     href: 'https://maxcdn.icons8.com/fonts/line-awesome/1.1/css/line-awesome.min.css'
                    // },
                    // {rel: 'stylesheet', href: '/css/vendors.css'},
                    // {rel: 'stylesheet', href: '/css/app.css'},
                    // {rel: 'stylesheet', href: '/css/core/menu/menu-types/vertical-menu.css'},
                    // {rel: 'stylesheet', href: '/css/core/colors/palette-gradient.css'},
                    {rel: 'stylesheet', href: '/css/pages/login-register.css'},
                ],
                // meta: {
                //     viewport: 'width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui'
                // },
                bodyAttrs: {
                    class: 'vertical-layout vertical-menu 1-column  bg-full-screen-image menu-expanded blank-page blank-page',
                    ['data-open']: 'click',
                    ['data-menu'] : 'vertical-menu',
                    ['data-color']  : 'bg-gradient-x-purple-blue',
                    ['data-col']: '1-column'
                },
                script: [
                    // { src:'/vendors/js/vendors.min.js'},
                    // { src:'/js/core/app-menu.js'},
                    // { src:'/js/core/app.js'},
                    // { src:'/vendors/js/forms/validation/jqBootstrapValidation.js'},
                    // { src:'/js/scripts/forms/form-login-register.js'},
                ],
            }
        }
    }
</script>

<style scoped>
</style>