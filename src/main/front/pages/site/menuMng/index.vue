<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header">
                        <h4 class="card-title">Project Revenue</h4>
                        <a class="heading-elements-toggle"><i class="la la-ellipsis-v font-medium-3"></i></a>
                        <div class="heading-elements">
                            <ul class="list-inline float-right">
                                <li><b-button @click="createUpMenu">상위메뉴등록</b-button></li>
                                <li><b-button @click="createMenu">등록</b-button></li>
                            </ul>
                        </div>
                    </div>
                    <div class="card-content collapse show pt-sm-1">
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
        <new-up-menu :selected-menu="selectedMenu"/>
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
        // async asyncData(){
        //     try {
        //         const response = await axios.post('/api/menu/getMenuList');
        //         console.log(response.data);
        //         // this.items = response.data.data;
        //         return {
        //             items : response.data.data
        //         }
        //     } catch (e) {
        //         //this.$bvModal.msgBoxOk(e.message());
        //     }
        // },
        beforeMount(){
            this.refreshGrid();
        },
        created() {
            debugger;
            console.dir(this);
            this.$eventBus.$on('refreshGrid', this.refreshGrid)
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
            async refreshGrid() {
                try {
                    const response = await axios.post('/api/menu/getMenuList', { searchUseYn: this.searchUseYn});

                    console.log(response.data);
                    this.items = response.data.data;
                } catch (e) {
                    this.$bvModal.msgBoxOk(e.message);
                }
            }
        }
    }
</script>

<style scoped>

</style>