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
                                    <li><b-button @click="createRoleInfo" class="btn-primary">등록</b-button></li>
                                </ul>
                            </div>
                            <div class="card-body table-responsive">
                                <b-table
                                        selectable
                                        :select-mode="'single'"
                                        @row-selected="onRowSelected"
                                        :items="roleItems"
                                        :fields="roleFields"
                                        ref="roleTable"
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
                                    <li><b-button @click.prevent="saveMenuMappingInfo" class="btn-primary">저장</b-button></li>
                                </ul>
                            </div>
                            <div class="card-body table-responsive">
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
        <modal-auth-info
                :selected-auth-info="selectedAuthInfo"
                :state="modalState"
        />
    </section>
</template>

<script>
    import ModalAuthInfo from '~/components/pages/auth/ModalAuthInfo.vue';
    export default {
        layout: 'default',
        name: "index",
        components:{ModalAuthInfo},

        data() {
            return {
                selectedAuthInfo: {},
                modalState: '',
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

        async created() {
            this.$eventBus.$on('refreshAuthList', this.selectRoleMstList);
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

            async selectRoleMenuList(roleCode) {
                try {
                    const {data} = await this.$axios.post('/api/auth/selectRoleMenuList', { roleCode });
                    return  data.data;
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

            async insertRoleMenuList(roleCode, menuIdList) {
                try {
                    const {data} = await this.$axios.post('/api/auth/insertRoleMenuList', { roleCode, menuIdList});
                    await this.$bvModal.msgBoxOk(data.message);

                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async updateRoleInfo(item) {
                this.modalState = "UPDATE";
                this.selectedAuthInfo = item;
                this.$bvModal.show('modal_authInfo');
            },

            async createRoleInfo() {
                this.modalState = "CREATE";
                this.$bvModal.show('modal_authInfo');
            },

            async saveMenuMappingInfo() {
                const menuIdList = this.menuItems.filter(menu => menu.checked === 'Y')
                                                 .map(menu => menu.menuId);

                const selectedRole = this.roleItems.find((role,i)=>this.$refs.roleTable.isRowSelected(i));

                if(!selectedRole) {
                    await this.$bvModal.msgBoxOk('등록된 권한을 선택해주세요');
                    return;
                }

                const message = await this.insertRoleMenuList(selectedRole.roleCode, menuIdList);
                await this.$bvModal.msgBoxOk(message);
            },

            async onRowSelected(items) {
                const roleCode = items && items.length && items[0].roleCode;
                if(!roleCode) return;

                const menuIdListByRole = await this.selectRoleMenuList(roleCode);
                console.log('menuIdListByRole', menuIdListByRole);
                this.menuItems.forEach(menu => this.$set(menu, 'checked', 'N'));

                menuIdListByRole.forEach(role=>{
                    const menu = this.menuItems.find(menu => menu.menuId === role.menuId);
                    this.$set(menu, 'checked', 'Y');
                })
            }
        }
    }
</script>

<style scoped>

</style>