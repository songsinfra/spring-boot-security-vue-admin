<template>
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="p-1">
                        <h5 class="mb-0 text-uppercase">ERROR </h5>
                    </div>
                    <div id="collapse1" class="card-collapse">
                        <div class="card mb-0">
                            <div class="card">
                                <div class="card-header" id="headingAOne">
                                    <h5 class="mb-0">
                                        {{error.statusCode }}
                                        {{error.message}}
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>
</template>

<script>
    export default {
        props: ['error'],
        layout: 'default',
        name: "error",
        fetch ({ store, redirect }) {
            debugger;
            console.log(this.$props.error);
        },
    }
</script>

<style scoped>

</style>