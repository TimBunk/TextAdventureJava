!VERTEX
#version 330

layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec2 aTextureCoordinate;

out vec2 textureCoordinate;

uniform mat4 projection;
uniform mat4 model;

void main() {
  gl_Position = projection * model * vec4(aPosition, 0.0f, 1.0f);
  textureCoordinate = aTextureCoordinate;
}
!END

!FRAGMENT
#version 330

out vec4 color;

in vec2 textureCoordinate;

uniform sampler2D ourTexture;

void main() {
  color = texture(ourTexture, textureCoordinate);
}
!END
