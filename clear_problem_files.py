import pandas as pd
import os

# 读取Excel文件
excel_file = 'h:/xian_api/xian_api.xlsx'

try:
    # 读取Excel文件
    df = pd.read_excel(excel_file)
    
    # 检查是否存在file_path列
    if 'file_path' not in df.columns:
        print("错误：Excel文件中不存在'file_path'列")
        # 显示可用的列名
        print("可用的列名:", list(df.columns))
    else:
        # 获取file_path列的所有值，去除空值
        file_paths = df['file_path'].dropna().tolist()
        
        print(f"找到 {len(file_paths)} 个文件路径")
        
        # 清除每个文件的内容
        for file_path in file_paths:
            # 处理路径格式，移除可能的重复目录
            # 从文件路径中提取相对路径部分
            rel_path = file_path
            # 如果路径以'xian_api/'开头，移除这个前缀
            if rel_path.startswith('xian_api/'):
                rel_path = rel_path[len('xian_api/'):]
            # 构建正确的绝对路径
            file_path = os.path.join('h:/xian_api', rel_path)
            
            # 检查文件是否存在
            if os.path.exists(file_path):
                # 检查是否是文件而不是目录
                if os.path.isfile(file_path):
                    try:
                        # 清空文件内容
                        with open(file_path, 'w', encoding='utf-8') as f:
                            f.write('')
                        print(f"已成功清空文件: {file_path}")
                    except Exception as e:
                        print(f"清空文件失败: {file_path}, 错误: {str(e)}")
                else:
                    print(f"警告: {file_path} 不是一个文件")
            else:
                print(f"警告: 文件不存在: {file_path}")
                
    print("处理完成")
    
except Exception as e:
      print(f"处理过程中出错: {str(e)}")