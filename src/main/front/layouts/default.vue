<template>
    <div>
        <base-top/>
        <base-left-menu/>
        <div class="app-content content">
            <div class="content-wrapper">
                <div class="content-wrapper-before"></div>
                <div class="content-header row">
                    <div class="content-header-left col-md-6 col-12 mb-0">
                        <h3 class="content-header-title">{{menuName}}</h3>
                    </div>
                    <div class="content-header-right col-md-6 col-12">
                        <div class="breadcrumbs-top float-md-right" v-if="!!menuName">
                            <div class="breadcrumb-wrapper mr-1">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Home</a>
                                    </li>
                                    <li class="breadcrumb-item"><a href="#">{{parantMenuName}}</a>
                                    </li>
                                    <li class="breadcrumb-item active">{{menuName}}</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="content-body">
                    <nuxt/>
                </div>
            </div>
        </div>
<!--        <base-footer/>-->
    </div>
</template>

<script>
    import BaseLeftMenu from '~/components/layout/BaseLeftMenu.vue';
    import BaseTop from '~/components/layout/BaseTop.vue';
    import BaseFooter from '~/components/layout/BaseFooter.vue';

    export default {
        name: "default",

        components: {
            BaseTop,
            BaseLeftMenu,
            BaseFooter
        },

        fetch({store}) {
        },

        computed : {
            menuName(){
                return this.$store.state.currentMenu ? this.$store.state.currentMenu.menuName : "";
            },

            parantMenuName(){
                return this.$store.state.currentMenu ? this.$store.state.currentMenu.upMenuName : "";
            }
        },

        head() {
            return {
                bodyAttrs: {
                    class: 'vertical-layout vertical-menu 2-columns menu-expanded fixed-navbar',
                    ['data-col']: '2-column',
                    ['data-color']  : 'bg-gradient-x-purple-blue',
                    ['data-menu'] : 'vertical-menu'
                },
                script: [
                    { src:'/vendors/js/vendors.min.js'},
                    { src:'/js/core/app-menu.js'},
                    { src:'/js/core/app.js'},
                ],
            }
        },

    }
</script>

<style scoped>
</style>