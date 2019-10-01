<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="card">
                            <div class="card-header pb-0">
                                <h3>등록된 권한</h3>
                                <ul class="list-inline float-right m-0">
                                    <li><b-button @click="createRole">등록</b-button></li>
                                </ul>
                            </div>
                            <div class="card-body">
                                <b-table
                                        :items="roleItems"
                                        :fields="roleFields"
                                >
                                    <template v-slot:action="{item}">
                                        <b-button
                                                variant="danger"
                                                @click="updateRoleInfo(item)"
                                        >수정</b-button>
                                    </template>
                                </b-table>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="card">
                            <div class="card-header pb-0">
                                <h3>등록된 메뉴</h3>
                                <ul class="list-inline float-right m-0">
                                    <li><b-button @click="">저장</b-button></li>
                                </ul>
                            </div>
                            <div class="card-body">
                                <b-table
                                        :items="menuItems"
                                        :fields="menuFields"
                                >
                                    <template v-slot:select="{item}">
                                        <b-form-checkbox
                                                v-model="item.checked"
                                                name="checkbox-1"
                                                value="Y"
                                                unchecked-value="N"
                                        >
                                        </b-form-checkbox>
                                    </template>
                                </b-table>
                            </div>
                        </div>
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

        data() {
            return {
                roleItems: [],
                roleFields: [
                    {key: 'roleCode', label: '권한코드'},
                    {key: 'codeNm', label: '권한명'},
                    {key: 'useYn', label: '사용여부'},
                    {key: 'action', label: '수정'},
                ],

                menuItems: [],
                menuFields: [
                    {key: 'upMenuName', label: '1Depth'},
                    {key: 'menuName', label: '2Depth'},
                    {key: 'select', label: '사용여부'},
                ],
            };
        },

        async beforeMount() {
            await Promise.all([
                this.selectRoleMstList(),
                this.getRoleMenuList()
            ]);
        },

        methods:{
            async selectRoleMstList() {
                try {
                    const {data} = await this.$axios.post('/api/auth/selectRoleMstList', { useYn: ''});
                    this.roleItems = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async getRoleMenuList() {
                try {
                    const {data} = await this.$axios.post('/api/menu/getRoleMenuList', { useYn: ''});
                    this.menuItems = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async updateRoleInfo(item) {

            },

            async saveMenuMappingInfo() {
                const menuIdList = this.menuItems.filter(menu => menu.checked === 'Y')
                                                 .map(menu => menu.menuId);




            }



        }
    }
</script>

<style scoped>

</style>