# Chemical Tweaker

这个Mod可以通过配置文件添加自定义MEK化学品。会识别`config/chemicaltweaker`下的所有json格式文件。

每个json文件内为一个数组，数组元素的格式如下：
```json
{
  "name": <化学品名称>,
  "type": <化学品类型，可选值有"gas"（气体），"slurry"（泥浆），"pigment"（颜料）和"infuse_type"（灌注类型）>
  "tint": <颜色，格式为"#rrggbb">
  "hidden": [是否隐藏，类型为boolean],
  "texture": [自定义材质，不填则为默认材质（对于泥浆，不能留空，需要从自定义材质或者默认材质"dirty"和"clean"之中选择一项）],
  "oreTag": [只有泥浆需要此属性，填入一个物品标签，即为泥浆在化学结晶器中显示的对应矿物],
  "attributes": [只有气体需要此属性，填入一个气体Attribute数组]
}
```

气体Attribute分为四种，其格式分别如下：

```json
{
  "type": "fuel",
  "burnTicks": <燃烧时长>,
  "energyDensity": <能量密度，注意需要以字符串而非数字形式提供>
}
```

```json
{
  "type": "radiation",
  "radioactivity": <放射性，类型为number>
}
```

```json
{
  "type": "cooled_coolant",
  "heatedGas": <对应的热冷却剂的名称>,
  "thermalEnthalpy": <焓，类型为number>
  "conductivity": <热导率，类型为number>          
}
```

```json
{
  "type": "heated_coolant",
  "heatedGas": <对应的冷却剂的名称>,
  "thermalEnthalpy": <焓，类型为number>
  "conductivity": <热导率，类型为number>          
}
```

以下是一个示例：

```json
[
  {
    "name": "some_gas",
    "type": "gas",
    "tint": "#ff0000",
    "attributes": [
      {
        "type": "fuel",
        "burnTicks": 100,
        "energyDensity": "10"
      }
    ]
  },
  {
    "name": "some_slurry",
    "type": "slurry",
    "tint": "#00ff00",
    "texture": "dirty",
    "oreTag": "#some_mod:some_ore"
  },
  {
    "name": "some_pigment",
    "type": "pigment",
    "tint": "#0000ff"
  }
]
```