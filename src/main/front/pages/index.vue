<template>
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-content collapse show">
                        <div style="text-align: center;" class="card-body card-dashboard">
                            <p class="card-text" id="tete">GFN 관리자 환영합니다.</p>
                            <button @click.prevent="test">테스트</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>
<script>
    export default {
        layout: 'default',

        beforeCreate() {
            this.$store.dispatch('login/getUser');
        },

        data() {
            return {
                project: "default"
            }
        },

        // async asyncData({$axios, error, store}) {
        //     try {
        //         const res = await $axios.$post(process.env.contextPath + '/front/constant', {test: 'test'});
        //         console.log(`res : ${res}`);
        //     } catch (e) {
        //         error({stateCode: 404, message: e.message});
        //     }
        //     return {
        //         name: "kim"
        //     }
        // },
        methods: {
            async test() {
                const res = await this.$axios.$post(process.env.contextPath + '/menu/getLayoutMenuList', {test: 'test'});
                console.log(res);
            },

            async getMemberInfo() {

            }
        }
    }

</script>

<style>

</style>