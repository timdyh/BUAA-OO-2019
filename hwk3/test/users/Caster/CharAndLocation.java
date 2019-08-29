package derivation;

class CharAndLocation {
    private Integer id;
    private Character character;

    CharAndLocation(Integer id, Character character) {
        this.id = id;
        this.character = character;
    }

    Integer GetId() {
        return this.id;
    }

    Character GetCharacter() {
        return this.character;
    }
}
