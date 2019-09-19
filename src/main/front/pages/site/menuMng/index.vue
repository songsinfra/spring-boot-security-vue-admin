<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header p-1">
                        <ul class="list-inline float-right m-0">
                            <li class="mr-1"><b-form-checkbox v-model="searchUseYn" value="Y"
                                                              unchecked-value="" @change="changeChckbox">미사용메뉴표시</b-form-checkbox></li>
                            <li><b-button @click="createUpMenu">상위메뉴등록</b-button></li>
                            <li><b-button @click="createMenu">등록</b-button></li>
                        </ul>
                    </div>
                    <div class="card-content collapse show">
                        <b-table :items="items"
                                 :fields="fields"
                        >
                            <template v-slot:action="{item}">
                                <b-button v-if="!item.menuName" variant="danger" @click="updateUpMenu(item)">수정</b-button>
                                <b-button v-if="item.menuName" variant="danger" @click="updateMenu(item)">수정</b-button>
                            </template>
                        </b-table>
                    </div>
                </div>
            </div>
        </div>
        <new-menu :selected-menu="selectedMenu" :state="modalState"/>
        <new-up-menu :selected-menu="selectedMenu" :state="modalState"/>
    </section>

</template>

<script>
    import NewMenu from '~/components/menu/newMenu.vue';
    import NewUpMenu from '~/components/menu/newUpMenu.vue';
    import axios from 'axios';

    export default {
        name: "index",
        layout: 'default',
        components: {NewMenu, NewUpMenu},
        async fetch({store}) {
            console.log("menuMng fetch");

        },
        data(){
            return {
                fields: [
                    { key: 'upMenuName', label : '1Depth'},
                    { key: 'menuName', label : '2Depth'},
                    { key: 'useYn', label : '사용여부',
                        formatter:(value)=> value === 'Y' ? '사용' : '미사용'
                    },
                    { key: 'ordNo', label : '정렬값'},
                    { key: 'action', label : '수정'},
                ],
                items: [],
                selectedMenu : {},
                form:{},
                searchUseYn:"",
                modalState: ""
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
            createUpMenu(item) {
                this.selectedMenu = item;
                this.modalState = "CREATE";
                this.$bvModal.show('modal_update_up_menu');
            },
            updateUpMenu(item) {
                this.selectedMenu = item;
                this.modalState = "UPDATE";
                this.$bvModal.show('modal_update_up_menu');
            },
            createMenu(item) {
                this.selectedMenu = item;
                this.modalState = "CREATE";
                this.$bvModal.show('modal_update_menu');
            },
            updateMenu(item) {
                this.selectedMenu = item;
                this.modalState = "UPDATE";
                this.$bvModal.show('modal_update_menu');
            },
            changeChckbox() {
                this.$nextTick(_=>{
                    this.refreshGrid();
                })
            },
            async refreshGrid() {
                try {
                    const response = await axios.post('/api/menu/getMenuList', { searchUseYn: this.searchUseYn});

                    console.log(response.data);
                    this.items = response.data.data;
                } catch (e) {
                   await this.$bvModal.msgBoxOk(e.message);
                }
            }
        }
    }
</script>

<style scoped>

</style>