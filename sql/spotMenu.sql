use `ry-vue`;
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('景点', '3', '1', 'spot', 'spot/spot/index', 1, 0, 'C', '0', '0', 'spot:spot:list', '#', 'admin', sysdate(), '', null, '景点菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('景点查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'spot:spot:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('景点新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'spot:spot:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('景点修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'spot:spot:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('景点删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'spot:spot:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('景点导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'spot:spot:export',       '#', 'admin', sysdate(), '', null, '');