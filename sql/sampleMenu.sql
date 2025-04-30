-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('水样', '0', '1', 'sample', 'sample/sample/index', 1, 0, 'C', '0', '0', 'sample:sample:list', '#', 'admin', sysdate(), '', null, '水样菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('水样查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'sample:sample:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('水样新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'sample:sample:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('水样修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'sample:sample:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('水样删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'sample:sample:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('水样导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'sample:sample:export',       '#', 'admin', sysdate(), '', null, '');