use `ry-vue`;
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文化', '3', '1', 'promotions', 'promotions/promotions/index', 1, 0, 'C', '0', '0', 'promotions:promotions:list', '#', 'admin', sysdate(), '', null, '文化菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文化查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'promotions:promotions:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文化新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'promotions:promotions:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文化修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'promotions:promotions:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文化删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'promotions:promotions:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文化导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'promotions:promotions:export',       '#', 'admin', sysdate(), '', null, '');