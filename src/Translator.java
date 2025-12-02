public record Translator(Dictionary dict) {

    public String translate(String phrase) {
        if (phrase == null || phrase.isBlank()) return "";

        String lower = phrase.toLowerCase();
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < phrase.length()) {
            boolean translated = false;

            for (String key : dict.getAllKeys()) {
                if (i + key.length() <= phrase.length() &&
                        lower.substring(i, i + key.length()).equals(key) &&
                        (i + key.length() == phrase.length() ||
                                !Character.isLetterOrDigit(lower.charAt(i + key.length())))) {

                    String original = phrase.substring(i, i + key.length());
                    String translation = dict.get(key);

                    if (Character.isUpperCase(original.charAt(0))) {
                        translation = translation.substring(0, 1).toUpperCase() +
                                (translation.length() > 1 ? translation.substring(1) : "");
                    }

                    result.append(translation);
                    i += key.length();
                    translated = true;

                    while (i < phrase.length() && !Character.isLetterOrDigit(phrase.charAt(i))) {
                        result.append(phrase.charAt(i++));
                    }
                    break;
                }
            }

            if (!translated) {
                int start = i;
                while (i < phrase.length() && Character.isLetterOrDigit(phrase.charAt(i))) i++;
                String word = phrase.substring(start, i);
                String clean = word.toLowerCase().replaceAll("[^a-z']", "");
                String tr = dict.get(clean);
                if (tr != null) {
                    result.append(preserveCase(word, tr));
                } else {
                    result.append("[").append(clean).append("]");
                }

                while (i < phrase.length() && !Character.isLetterOrDigit(phrase.charAt(i))) {
                    result.append(phrase.charAt(i++));
                }
            }
        }
        return result.toString().trim();
    }

    private String preserveCase(String original, String translation) {
        if (original.matches("[A-Z]+")) return translation.toUpperCase();
        if (Character.isUpperCase(original.charAt(0))) {
            return translation.substring(0, 1).toUpperCase() +
                    (translation.length() > 1 ? translation.substring(1).toLowerCase() : "");
        }
        return translation.toLowerCase();
    }
}