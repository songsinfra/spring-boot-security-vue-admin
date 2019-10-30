<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0">
                        <ul class="list-inline float-right m-0">
                            <li class="mr-1 custom-control custom-checkbox"><b-form-checkbox
                                    v-model="searchUseYn"
                                    value="1"
                                    unchecked-value=""
                                    @change="changeCheckbox"
                            >미사용서비스표시</b-form-checkbox>
                            </li>
                            <li><b-button @click="createAddInfo" class="btn-primary">등록</b-button></li>
                        </ul>
                    </div>
                    <div class="card-content collapse show">
                        <div class="card-body">
                            <div class="table-responsive">
                                <b-table
                                        :items="items"
                                        :fields="fields"
                                        :per-page="perPage"
                                        :current-page="currentPage"
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
                                <b-pagination
                                        v-model="currentPage"
                                        :total-rows="rows"
                                        :per-page="perPage"
                                        aria-controls="my-table"
                                        :align="'center'"
                                ></b-pagination>
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
    import JsonExcel from 'vue-json-excel'

    export default {
        name: "index",
        layout: 'default',
        components: {ModalAddInfo, downloadExcel: JsonExcel},

        computed: {
            rows() {
                return this.items.length
            }
        },

        data() {
            return {
                fields: [
                    'index',
                    {key: 'addItemNm', label: '부가서비스명', tdClass:'pr-0'},
                    {key: 'addItemCode', label: '부가서비스코드'},
                    {key: 'createDt', label: '등록일',
                        formatter: (value) => value && value.substring(0, 10)
                    },
                    {key: 'statusCd', label: '상태코드',
                        formatter:(value)=> value === '1' ? '사용' : '미사용'},
                    {key: 'action', label: '수정'},
                ],
                items: [],
                selectedAddInfo: {},
                modalState: '',
                searchUseYn: '',
                perPage: 10,
                currentPage: 1,
            }
        },

        beforeMount() {
            this.getAddItemList();
        },

        async created() {
            this.$eventBus.$on('refreshAddList', this.getAddItemList);
        },

        methods:{
            async getAddItemList() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/add/selectAddItemList', { statusCd : this.searchUseYn});
                    this.items = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            changeCheckbox() {
                this.$nextTick(_=>{
                    this.getAddItemList();
                })
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

            downloadExcel() {

            }
        }

    }
</script>

<style scoped>

</style>