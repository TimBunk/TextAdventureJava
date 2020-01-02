!VERTEX
#version 330

layout (location = 0) in vec2 l_position;
layout (location = 1) in vec2 l_textureCoordinate;

out vec2 in_textureCoordinate;
out vec4 in_color;

uniform vec4 u_color;
uniform mat4 u_projection;
uniform mat4 u_model;

void main() {
  gl_Position = u_projection * u_model * vec4(l_position, 0.0f, 1.0f);
  in_textureCoordinate = l_textureCoordinate;
  in_color = u_color;
}
!END

!FRAGMENT
#version 330

out vec4 fragColor;

in vec2 in_textureCoordinate;
in vec4 in_color;

uniform sampler2D u_texture;

void main() {

  fragColor = texture(u_texture, in_textureCoordinate);
  fragColor = vec4(in_color.r, in_color.g, in_color.b, in_color.a * fragColor.a);
  //fragColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);

}
!END
