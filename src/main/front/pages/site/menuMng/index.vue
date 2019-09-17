<template>
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="p-1">
                        <p><span class="text-bold-600">Example 1:</span> Table with outer spacing</p>
                        <b-table :items="items"
                                 :fields="fields"
                                 @row-clicked="onRowSelected"
                                 selectable
                                 :select-mode="'single'"
                        >
                        </b-table>
                    </div>
                </div>
            </div>
        </div>
        <b-modal id="modal_update_menu" title="메뉴 수정">
            <b-form>
                <b-form-group
                        id="upMenuId-group-1"
                        label="상위메뉴:"
                        label-for="upMenuId-1"
                >
                    <b-form-input
                            id="upMenuId-1"
                            v-model="selectedMenu.upMenuId"
                            required
                    ></b-form-input>
                </b-form-group>
                <b-form-group
                        id="menuName-group-1"
                        label="메뉴명:"
                        label-for="menuName-1"
                        description="We'll never share your email with anyone else."
                >
                    <b-form-input
                            id="menuName-1"
                            v-model="selectedMenu.menuName"
                            required
                    ></b-form-input>
                </b-form-group>
                <b-form-group
                        id="ordNo-group-1"
                        label="정렬값:"
                        label-for="ordNo-1"
                >
                    <b-form-input
                            id="ordNo-1"
                            v-model="selectedMenu.ordNo"
                            required
                    ></b-form-input>
                </b-form-group>
                <b-form-group
                        id="useYn-group-1"
                        label="사용여부:"
                        label-for="useYn-1"
                >
                    <b-form-input
                            id="useYn-1"
                            v-model="selectedMenu.useYn"
                            required
                    ></b-form-input>
                </b-form-group>
                <b-form-group
                        id="input-group-1"
                        label="URL:"
                        label-for="menuURL-1"
                >
                    <b-form-input
                            id="menuURL-1"
                            v-model="selectedMenu.menuURL"
                            required
                    ></b-form-input>
                </b-form-group>
            </b-form>
        </b-modal>
    </section>
</template>

<script>
    export default {
        name: "index",
        layout: 'default',
        data(){
            return {
                fields: ['menuId', 'menuName', 'upMenuId', 'useYn', 'ordNo'],
                items: [],
                selectedMenu : {},
                form:{}
            }
        },
        async asyncData({$axios, error}){
            try {
                const response = await $axios.$post('/api/menu/getMenuList');
                // debugger;
                return {
                    items : response.data
                };
            } catch (e) {
                //this.$bvModal.msgBoxOk(e.message());
            }
        },
        methods: {
            onRowSelected(item, test) {
                this.selectedMenu = item;
                this.$bvModal.show("modal_update_menu");
            }
        }
    }
</script>

<style scoped>

</style>