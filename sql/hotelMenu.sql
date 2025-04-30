use `ry-vue`;
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('酒店表', '3', '1', 'hotel', 'hotel/hotel/index', 1, 0, 'C', '0', '0', 'hotel:hotel:list', '#', 'admin', sysdate(), '', null, '酒店表菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('酒店表查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'hotel:hotel:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('酒店表新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'hotel:hotel:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('酒店表修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'hotel:hotel:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('酒店表删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'hotel:hotel:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('酒店表导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'hotel:hotel:export',       '#', 'admin', sysdate(), '', null, '');