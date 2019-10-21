<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header p-1">
                        <ul class="list-inline float-right m-0">
                            <li><b-button @click="createAddInfo" class="btn-primary">등록</b-button></li>
                        </ul>
                    </div>
                    <div class="card-content collapse show">
                        <div class="card-body">
                            <div class="table-responsive">
                                <b-table
                                        :items="items"
                                        :fields="fields"
                                >
                                    <template v-slot:index="data">
                                        {{ data.index + 1 }}
                                    </template>
                                    <template v-slot:action="{item}">
                                        <b-button
                                                variant="danger"
                                                @click="updateAddInfo(item)"
                                        >수정</b-button>
                                    </template>
                                </b-table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <modal-add-info
                :selected-add-info="selectedAddInfo"
                :state="modalState"
        />
    </section>
</template>



<script>
    import ModalAddInfo from '~/components/pages/add/ModalAddInfo.vue';

    export default {
        name: "index",
        layout: 'default',
        components: {ModalAddInfo},

        data() {
            return {
                fields: [
                    'index',
                    {key: 'addItemNm', label: '부가서비스명', tdClass:'pr-0'},
                    {key: 'addItemCode', label: '부가서비스코드'},
                    {key: 'createDt', label: '등록일',
                        formatter: (value) => value && value.substring(0, 10)
                    },
                    {key: 'action', label: '수정'},
                ],
                items: [],
                selectedAddInfo: {},
                modalState: ''
            }
        },

        beforeMount() {
            this.getAddItemList();
        },

        async created() {
            this.$eventBus.$on('refreshAddList', this.getAddItemList);
        },

        methods:{
            async getAddItemList(addItemNm) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/add/selectAddItemList', { addItemNm});
                    this.items = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async createAddInfo() {
                this.modalState = "CREATE";
                this.errors.clear();
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_addInfo');
                });
            },

            async updateAddInfo(item) {
                this.modalState = "UPDATE";
                this.selectedAddInfo = item;
                this.errors.clear();
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_addInfo');
                });
            },
        }

    }
</script>

<style scoped>

</style>