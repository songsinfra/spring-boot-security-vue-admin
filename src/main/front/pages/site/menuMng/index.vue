<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0">
                        <div class="form-row float-right align-items-center">
                            <div class="col-auto">
                                <b-form-checkbox
                                        v-model="searchUseYn"
                                        value="Y"
                                        unchecked-value=""
                                        @change="changeCheckbox"
                                >미사용메뉴표시</b-form-checkbox>
                            </div>
                            <div class="col-auto">
                                <b-button @click="createUpMenu" class="btn-primary">상위메뉴등록</b-button>
                            </div>
                            <div class="col-auto">
                                <b-button @click="createMenu" class="btn-primary">등록</b-button>
                            </div>
                        </div>
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
                                    <template v-slot:action="{item}">
                                        <b-button
                                                v-if="!item.menuName"
                                                variant="danger"
                                                @click="updateUpMenu(item)"
                                        >수정</b-button>
                                        <b-button
                                                v-if="item.menuName"
                                                variant="danger"
                                                @click="updateMenu(item)"
                                        >수정</b-button>
                                    </template>
                                </b-table>
                                <b-pagination
                                        v-model="currentPage"
                                        :total-rows="tableRows"
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
        <modal-menu
                :selected-menu="selectedMenu"
                :state="modalState"
        />
        <modal-up-menu
                :selected-menu="selectedMenu"
                :state="modalState"
        />
    </section>

</template>

<script>
    import ModalMenu from '~/components/pages/menu/ModalMenu.vue';
    import ModalUpMenu from '~/components/pages/menu/ModalUpMenu.vue';

    export default {
        name: "index",
        layout: 'default',
        components: {ModalMenu, ModalUpMenu},

        async fetch({store}) {
            console.log("menuMng fetch");
        },

        computed: {
            tableRows() {
                return this.items.length
            }
        },

        data(){
            return {
                fields: [
                    { key: 'upMenuName', label : '1Depth', tdClass:'pr-0'},
                    { key: 'menuName', label : '2Depth', tdClass:'pr-0'},
                    { key: 'useYn', label : '사용여부',
                        formatter:(value)=> value === 'Y' ? '사용' : '미사용'
                    },
                    { key: 'ordNo', label : '정렬값'},
                    { key: 'action', label : '수정'},
                ],
                items: [],
                form:{},
                searchUseYn:"",
                selectedMenu : {},
                modalState: "",
                perPage: 10,
                currentPage: 1,
            }
        },

        beforeMount(){
            this.refreshGrid();
        },

        async created() {
            this.$eventBus.$on('refreshGrid', this.refreshGrid);
            await this.$store.dispatch('menu/addUpMenuList');
        },

        methods: {
            createUpMenu() {
                this.modalState = "CREATE";
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_update_up_menu');
                });
            },

            updateUpMenu(item) {
                this.selectedMenu = item;
                this.modalState = "UPDATE";
                this.$nextTick(async () => {
                this.$bvModal.show('modal_update_up_menu');
                });
            },

            createMenu() {
                this.modalState = "CREATE";
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_update_menu');
                });
            },

            updateMenu(item) {
                this.selectedMenu = item;
                this.modalState = "UPDATE";
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_update_menu');
                });
            },

            changeCheckbox() {
                this.$nextTick(_=>{
                    this.refreshGrid();
                })
            },

            async refreshGrid() {
                try {
                    const response = await this.$axios.post(process.env.contextPath + '/menu/getMenuList', { searchUseYn: this.searchUseYn});
                    this.items = response.data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                   await this.$bvModal.msgBoxOk(e.message);
                }
            }
        }
    }
</script>

<style scoped>

</style>