<template>
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="p-1">
                        <h5 class="mb-0 text-uppercase">Auth</h5>
                        <p>auth</p>
                        <button @click="popupMsg">팝업</button>
                    </div>
                </div>
            </div>
        </div>
        <modals-container/>
    </section>
</template>

<script>
    import modalMsg from '~/components/auth/modalMsg';

    export default {
        layout: 'defautl',
        name: "index",
        methods:{
            popupMsg() {
                this.$modal.show(modalMsg);
            }
        }
    }
</script>

<style scoped>

</style>