<template>
    <b-modal id="modal_update_up_menu" centered title="메뉴 수정">
        <b-form>
            <b-form-group
                    id="upMenuName-group-1"
                    label="상위메뉴명:"
                    label-for="upMenuName-1"
            >
                <b-form-input
                        id="upMenuName-1"
                        v-model="selectedMenu.upMenuName"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="menuName-group-1"
                    label="사용여부:"
                    label-for="menuName-1"
                    description="We'll never share your email with anyone else."
            >
                <b-form-input
                        id="menuName-1"
                        v-model="selectedMenu.useYn"
                        required
                ></b-form-input>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        props: ['selectedMenu'],
        name: "newUpMenu",
        data() {
            return {
            };
        },
        async created() {
            if(!this.$props.selectedMenu.upMenuName) return;

            const upMenuList = await this.$axios.$post('/api/menu/getUpMenuList')

            debugger;
            console.log(upMenuList);
        }
    }
</script>

<style scoped>

</style>